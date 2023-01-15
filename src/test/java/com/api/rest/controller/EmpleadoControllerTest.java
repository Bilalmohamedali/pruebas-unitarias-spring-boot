package com.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.api.rest.model.Empleado;
import com.api.rest.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGuardarEmpleado() throws Exception {
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();

        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados()throws Exception{

        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Christian").apellido("Ramirez").email("c1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Gabriel").apellido("Ramirez").email("g1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Julen").apellido("Ramirez").email("cj@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Biaggio").apellido("Ramirez").email("b1@gmail.com").build());
        listaEmpleados.add(Empleado.builder().nombre("Adrian").apellido("Ramirez").email("a@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        ResultActions response = mockMvc.perform(get("/api/empleados"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(listaEmpleados.size())));
    }

    @Test
    void testObtenerEmpleadoPorId() throws Exception {
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));

        ResultActions response = mockMvc.perform(get("/api/empleados/{id}",empleadoId));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }

    @Test
    void testObtenerEmpleadoNoEncontrado() throws Exception {

        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/empleados/{id}",empleadoId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testActualizarEmpleado() throws Exception {

        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Andres")
                .apellido("Guerra")
                .email("AG13@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.uptadeEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));


    }
    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception {

        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Eduardo")
                .apellido("Elric")
                .email("EE@gmail.com")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Andres")
                .apellido("Guerra")
                .email("AG13@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.uptadeEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testEliminarEmpleado() throws Exception {

        long empleadoId = 1L;
        willDoNothing().given(empleadoService).deteleEmpleado(empleadoId);

        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}",empleadoId));

        response.andExpect(status().isOk())
                .andDo(print());

    }

}
