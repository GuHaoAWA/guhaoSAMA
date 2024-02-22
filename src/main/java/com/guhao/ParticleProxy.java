
package com.guhao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ParticleProxy {

    public static void main(String[] args) {
        Particle originalParticle = new OriginalParticle();
        Particle particleWithoutCulling = createParticleWithoutCulling(originalParticle);

        System.out.println("Original Particle shouldCull: " + originalParticle.shouldCull()); // true
        System.out.println("Proxy Particle shouldCull: " + particleWithoutCulling.shouldCull()); // false
    }

    public interface Particle {
        boolean shouldCull();
    }

    public static class OriginalParticle implements Particle {
        @Override
        public boolean shouldCull() {
            return true;
        }
    }

    public static Particle createParticleWithoutCulling(Particle originalParticle) {
        return (Particle) Proxy.newProxyInstance(
                ParticleProxy.class.getClassLoader(),
                new Class[]{Particle.class},
                new NoCullHandler(originalParticle)
        );
    }

    public static class NoCullHandler implements InvocationHandler {
        private final Particle target;

        public NoCullHandler(Particle target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("shouldCull")) {
                return false; // 禁用边界框裁剪
            } else {
                return method.invoke(target, args);
            }
        }
    }
}
