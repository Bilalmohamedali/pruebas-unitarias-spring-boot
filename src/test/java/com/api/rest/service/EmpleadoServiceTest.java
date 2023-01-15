package com.api.rest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.model.Empleado;
import com.api.rest.repository.EmpleadoRepository;
import com.api.rest.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest  {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Cuesta")
                .email("JCuesta@gmail.com")
                .build();

    }

    @DisplayName("Test guardar empleado")
    @Test
    void testGuardarEmpleado() {

        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado)).willReturn(empleado);

        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);

        assertThat(empleadoGuardado).isNotNull();

    }

    @DisplayName("Test guardar empleado con Throw exception")
    @Test
    void testGuardarEmpleadoConThrowExcption() {

        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));


        assertThrows(ResourceNotFoundException.class,() -> {
            empleadoService.saveEmpleado(empleado);
        });

        verify(empleadoRepository,never()).save(any(Empleado.class));
    }

    @DisplayName("Test para listar empleados")
    @Test
    void testListarEMpleados() {

        Empleado empleado1 = Empleado.builder()
                .id(2L)
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));

        List<Empleado> empleados = empleadoService.getAllEmpleados();

        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para retornar una lista vacia")
    @Test
    void testListarColeccionEmpleadosVacia() {

        Empleado empleado1 = Empleado.builder()
                .id(2L)
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();

        given(empleadoRepository.findAll()).willReturn(Collections.EMPTY_LIST);

        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();

        assertThat(listaEmpleados).isEmpty();
        assertThat(listaEmpleados.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener empleado por ID")
    @Test
    void testObtenerEmpleadoPorId() {

        given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));

        Empleado empleadoGuardado = empleadoService.getEmpleadoById(empleado.getId()).get();

        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleado(){

        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("JCuesta13@gmail.com");
        empleado.setNombre("Juanito");

        Empleado empleadoAcualizado = empleadoService.uptadeEmpleado(empleado);

        assertThat(empleadoAcualizado.getEmail()).isEqualTo("JCuesta13@gmail.com");
        assertThat(empleadoAcualizado.getNombre()).isEqualTo("Juanito");
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    void testEliminarEmpleado(){

        long empleadoId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);

        empleadoService.deteleEmpleado(empleadoId);

        verify(empleadoRepository,times(1)).deleteById(empleadoId);

    }

}
