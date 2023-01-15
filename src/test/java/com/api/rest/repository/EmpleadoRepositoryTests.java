package com.api.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.api.rest.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmpleadoRepositoryTests {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .nombre("Juan")
                .apellido("Cuesta")
                .email("JCuesta@gmail.com")
                .build();

    }


    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado()  {

        Empleado empleado1  = Empleado.builder()
                .nombre("Juan")
                .apellido("Lopez")
                .email("juanlopez13@gmail.com")
                .build();

        Empleado empleadoGuardado = empleadoRepository.save(empleado1);

        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar empleados")
    @Test
    void testListarEmpleados() {

        Empleado empleado1  = Empleado.builder()
                .nombre("Andres")
                .apellido("Guerra")
                .email("andrescms13@gmail.com")
                .build();

        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);


        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados).size().isEqualTo(2);
    }

    @DisplayName("Test para obtener un empleado por ID")
    @Test
    void testObtenerEmpleadoPorId() {
        empleadoRepository.save(empleado);

        Empleado empleadoBd = empleadoRepository.findById(empleado.getId()).get();

        assertThat(empleadoBd).isNotNull();

    }

    @DisplayName("Test para Actualizar a un empleado ")
    @Test
    void testActualizarEmpleado(){
        empleadoRepository.save(empleado);


        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("J321@gmail.com");
        empleadoGuardado.setNombre("JJ");
        empleadoGuardado.setApellido("Redick");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        assertThat(empleadoActualizado.getEmail()).isEqualTo("J321@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("JJ");

    }

    @DisplayName("Test para eliminar empleado")
    @Test
    void testEliminarEmpleado() {
        empleadoRepository.save(empleado);


        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        assertThat(empleadoOptional).isEmpty();
    }


}
