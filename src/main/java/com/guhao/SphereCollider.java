package com.guhao;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

public class SphereCollider extends Collider {
    private final float radius;

    public SphereCollider(Vec3 center, float radius) {
        super(center, createInitialAABB(center, radius));
        this.radius = radius;
    }


    private static AABB createInitialAABB(Vec3 center, float radius) {
        return new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius
        );
    }

    @Override
    protected void transform(OpenMatrix4f mat) {
        super.transform(mat);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void drawInternal(PoseStack matrixStack, MultiBufferSource buffer, OpenMatrix4f mat, boolean red) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, red ? 0.0F : 1.0F, red ? 0.0F : 1.0F, 1.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lines());
        matrixStack.pushPose();
        MathUtils.translateStack(matrixStack, mat);
        Matrix4f matrix = matrixStack.last().pose();

        drawMeridians(matrix, vertexConsumer, red);
        drawParallels(matrix, vertexConsumer, red);
//        float color2 = red ? 0.0F : 1.0F;
//        RenderSystem.enableDepthTest(); // 启用深度测试
//        RenderSystem.setShaderColor(1.0F, color2, color2, 1.0F); // 设置颜色
//        OpenMatrix4f transpose = new OpenMatrix4f();
//        OpenMatrix4f.transpose(mat, transpose);
//        matrixStack.pushPose();
//        MathUtils.translateStack(matrixStack, mat);
//        MathUtils.rotateStack(matrixStack, transpose);
//        Matrix4f matrix4f = matrixStack.last().pose();
//        // 绘制球体的线框
//        for (int i = 0; i < 360; i += 10) {
//            for (int j = 0; j < 360; j += 10) {
//                float x1 = (float) (this.worldCenter.x + this.radius * Math.cos(Math.toRadians(i)) * Math.cos(Math.toRadians(j)));
//                float y1 = (float) (this.worldCenter.y + this.radius * Math.sin(Math.toRadians(i)));
//                float z1 = (float) (this.worldCenter.z + this.radius * Math.cos(Math.toRadians(i)) * Math.sin(Math.toRadians(j)));
//
//                float x2 = (float) (this.worldCenter.x + this.radius * Math.cos(Math.toRadians(i + 10)) * Math.cos(Math.toRadians(j)));
//                float y2 = (float) (this.worldCenter.y + this.radius * Math.sin(Math.toRadians(i + 10)));
//                float z2 = (float) (this.worldCenter.z + this.radius * Math.cos(Math.toRadians(i + 10)) * Math.sin(Math.toRadians(j)));
//                float color = red ? 0.0F : 1.0F;
//                vertexConsumer
//                        .vertex(matrix4f, x1, y1, z1)
//                        .color(1.0f, color, color, 1.0f)
//                        .normal(0.0f, 1.0f, 0.0f)
//                        .endVertex();
//
//                vertexConsumer
//                        .vertex(matrix4f, x2, y2, z2)
//                        .color(1.0f, color, color, 1.0f)
//                        .normal(0.0f, 1.0f, 0.0f)
//                        .endVertex();
//            }
//        }
        matrixStack.popPose();
    }

    private void drawMeridians(Matrix4f matrix, VertexConsumer consumer, boolean red) {
        int steps = 36;
        float angleStep = 360.0F / steps;

        for (int i = 0; i < steps; i++) {
            float theta = (float) Math.toRadians(i * angleStep);
            float nextTheta = (float) Math.toRadians((i + 1) * angleStep);

            for (int j = 0; j <= 180; j += 10) {
                float phi = (float) Math.toRadians(j);

                Vec3 point = getSphericalPoint(theta, phi);
                Vec3 nextPoint = getSphericalPoint(nextTheta, phi);

                drawLine(matrix, consumer, point, nextPoint, red);
            }
        }
    }

    private void drawParallels(Matrix4f matrix, VertexConsumer consumer, boolean red) {
        int parallels = 18; // 纬线数量（包含赤道）
        float phiStep = (float) Math.PI / parallels; // 角度间隔

        for (int i = 1; i < parallels; i++) { // 跳过极点（i=0和i=parallels）
            float phi = i * phiStep; // 当前极角（0到π）
            int circlePoints = 36; // 每圈纬线顶点数
            float thetaStep = (float) (2 * Math.PI / circlePoints);

            Vec3 prevPoint = getSphericalPoint(0, phi); // 初始点
            for (int j = 1; j <= circlePoints; j++) {
                float theta = j * thetaStep; // 当前方位角
                Vec3 currPoint = getSphericalPoint(theta, phi);

                // 绘制线段连接前后点
                drawLine(matrix, consumer, prevPoint, currPoint, red);
                prevPoint = currPoint;
            }
        }
    }

    private Vec3 getSphericalPoint(float theta, float phi) {
        return new Vec3(
                radius * Math.sin(phi) * Math.cos(theta),
                radius * Math.cos(phi),
                radius * Math.sin(phi) * Math.sin(theta)
        );
    }

    private void drawLine(Matrix4f matrix, VertexConsumer consumer, Vec3 start, Vec3 end, boolean red) {
        float color = red ? 0.0F : 1.0F;
        consumer.vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
                .color(1.0F, red ? 0.0F : 1.0F, red ? 0.0F : 1.0F, 1.0F)
                .color(1.0f, color, color, 1.0f)
                .normal(0.0f, 1.0f, 0.0f)
                .endVertex();
        consumer.vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
                .color(1.0F, red ? 0.0F : 1.0F, red ? 0.0F : 1.0F, 1.0F)
                .color(1.0f, color, color, 1.0f)
                .normal(0.0f, 1.0f, 0.0f)
                .endVertex();
    }

    @Override
    protected AABB getHitboxAABB() {
        // 生成球体的立方体AABB用于快速筛选
        return new AABB(
                worldCenter.x - radius,
                worldCenter.y - radius,
                worldCenter.z - radius,
                worldCenter.x + radius,
                worldCenter.y + radius,
                worldCenter.z + radius
        );
    }
    // 在工具类或当前类中添加静态方法
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    @Override
    public boolean isCollide(Entity entity) {
        AABB entityAABB = entity.getBoundingBox();
        Vec3 sphereCenter = this.worldCenter;
        double sphereRadius = this.radius;

        double closestX = clamp(sphereCenter.x, entityAABB.minX, entityAABB.maxX);
        double closestY = clamp(sphereCenter.y, entityAABB.minY, entityAABB.maxY);
        double closestZ = clamp(sphereCenter.z, entityAABB.minZ, entityAABB.maxZ);

        double dx = sphereCenter.x - closestX;
        double dy = sphereCenter.y - closestY;
        double dz = sphereCenter.z - closestZ;

        return (dx * dx + dy * dy + dz * dz) <= (sphereRadius * sphereRadius);
    }


    @Override
    public Collider deepCopy() {
        return new SphereCollider(this.modelCenter, this.radius);
    }

    @Override
    public String toString() {
        return String.format("[SphereCollider] center: %s, radius: %.1f", modelCenter, radius);
    }
}