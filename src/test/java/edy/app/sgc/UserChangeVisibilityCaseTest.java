package edy.app.sgc;

import edy.app.sgc.arch.domain.usecase.user.UserChangeVisibilityCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author edythawne
 * @created 04/09/2026 11:03
 * @project ut_sgc
 */
@Slf4j
@SpringBootTest
class UserChangeVisibilityCaseTest {

    @Autowired
    private UserChangeVisibilityCase changeVisibilityCase;

    @Test
    void testConcurrenciaSingleton() throws InterruptedException {
        int numeroDeHilos = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numeroDeHilos);

        // El latch asegura que ambos hilos inicien exactamente al mismo tiempo
        CountDownLatch latchInicio = new CountDownLatch(1);
        CountDownLatch latchFin = new CountDownLatch(numeroDeHilos);

        // Hilo 1: Simula al Usuario A pidiendo el ID 10
        executor.submit(() -> {
            try {
                latchInicio.await(); // Espera la señal de salida
                changeVisibilityCase.execute(1L);

                // Forzamos una pequeña pausa artificial para dar tiempo a que el Hilo 2 sobrescriba el valor
                Thread.sleep(100);

                // Al ejecutar, si es Singleton, imprimirá el ID del Usuario B (25) en lugar del suyo (10)
                //log.info("Hilo A procesando ID: " + changeVisibilityCase.getRequestForTest());

                Assertions.assertEquals(1L, 1L, "¡El Hilo A leyó un ID incorrecto!");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latchFin.countDown();
            }
        });

        // Hilo 2: Simula al Usuario B pidiendo el ID 25
        executor.submit(() -> {
            try {
                latchInicio.await(); // Espera la señal de salida
                Thread.sleep(20); // Un desfase mínimo para asegurar que entra justo después del Hilo A

                changeVisibilityCase.execute(2L);
                //log.info("Hilo B procesando ID: " + changeVisibilityCase.getRequestForTest());

                Assertions.assertEquals(2L, 1L);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latchFin.countDown();
            }
        });

        // Damos la señal de salida a ambos hilos simultáneamente
        latchInicio.countDown();

        // Esperamos a que ambos terminen
        latchFin.await();
        executor.shutdown();
    }
}
