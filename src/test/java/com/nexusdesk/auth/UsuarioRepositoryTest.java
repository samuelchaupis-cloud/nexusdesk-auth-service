package com.nexusdesk.auth;

import com.nexusdesk.auth.entity.Rol;
import com.nexusdesk.auth.entity.Usuario;
import com.nexusdesk.auth.repository.RolRepository;
import com.nexusdesk.auth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private Rol rolTecnico;

    @BeforeEach
    void setUp() {
        rolTecnico = new Rol();
        rolTecnico.setNombre("Técnico");
        rolTecnico.setDescripcion("Personal de soporte en campo");
        rolRepository.save(rolTecnico);
    }

    @Test
    @DisplayName("Cibertec AP1 Test 1: Insertar operación en capa de acceso a datos")
    void testInsertarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setRol(rolTecnico);
        usuario.setNombres("Juan");
        usuario.setApellidos("Pérez");
        usuario.setUsuario("jperez");
        usuario.setEmail("jperez@techdesk.pe");
        usuario.setPassword("$2a$10$xyz123hashedPassword");
        usuario.setTelefono("987654321");
        usuario.setCargo("Técnico Junior");
        usuario.setEstado(1);

        Usuario saved = usuarioRepository.save(usuario);

        assertNotNull(saved.getId(), "El ID debe ser autogenerado por la BD");
        assertEquals("jperez", saved.getUsuario());
        assertEquals("Juan", saved.getNombres());
        assertEquals(1, saved.getEstado());
    }

    @Test
    @DisplayName("Cibertec AP1 Test 2: Listar operación en capa de acceso a datos")
    void testListarUsuarios() {
        Usuario u1 = new Usuario();
        u1.setRol(rolTecnico);
        u1.setNombres("Ana");
        u1.setApellidos("Gómez");
        u1.setUsuario("agomez");
        u1.setEmail("agomez@techdesk.pe");
        u1.setPassword("hash1");
        usuarioRepository.save(u1);

        Usuario u2 = new Usuario();
        u2.setRol(rolTecnico);
        u2.setNombres("Luis");
        u2.setApellidos("Flores");
        u2.setUsuario("lflores");
        u2.setEmail("lflores@techdesk.pe");
        u2.setPassword("hash2");
        usuarioRepository.save(u2);

        List<Usuario> lista = usuarioRepository.findAll();
        assertTrue(lista.size() >= 2, "La lista debe contener al menos los 2 usuarios insertados");

        Page<Usuario> page = usuarioRepository.searchUsers("Gómez", null, PageRequest.of(0, 10));
        assertEquals(1, page.getTotalElements());
        assertEquals("agomez", page.getContent().get(0).getUsuario());
    }

    @Test
    @DisplayName("Cibertec AP1 Test 3: Actualizar operación en capa de acceso a datos")
    void testActualizarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setRol(rolTecnico);
        usuario.setNombres("Carlos");
        usuario.setApellidos("Mendoza");
        usuario.setUsuario("cmendoza_test");
        usuario.setEmail("cmendoza_test@techdesk.pe");
        usuario.setPassword("hashInitial");
        usuario.setCargo("Técnico Jr");
        Usuario saved = usuarioRepository.save(usuario);

        // Modificar datos
        saved.setCargo("Técnico Senior de Redes");
        saved.setTelefono("999888777");
        Usuario updated = usuarioRepository.save(saved);

        Optional<Usuario> found = usuarioRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Técnico Senior de Redes", found.get().getCargo());
        assertEquals("999888777", found.get().getTelefono());
    }

    @Test
    @DisplayName("Cibertec AP1 Test 4: Eliminar operación en capa de acceso a datos")
    void testEliminarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setRol(rolTecnico);
        usuario.setNombres("Pedro");
        usuario.setApellidos("Navarro");
        usuario.setUsuario("pnavarro");
        usuario.setEmail("pnavarro@techdesk.pe");
        usuario.setPassword("hashDelete");
        Usuario saved = usuarioRepository.save(usuario);
        Long id = saved.getId();

        // Eliminar
        usuarioRepository.deleteById(id);

        Optional<Usuario> found = usuarioRepository.findById(id);
        assertFalse(found.isPresent(), "El usuario eliminado no debe existir en la BD");
    }

    @Test
    @DisplayName("Cibertec AP1 Test 5: Buscar por login o email activo")
    void testBuscarPorLoginOEmailActivo() {
        Usuario usuario = new Usuario();
        usuario.setRol(rolTecnico);
        usuario.setNombres("Lucía");
        usuario.setApellidos("Fernández");
        usuario.setUsuario("lfernandez_test");
        usuario.setEmail("lfernandez_test@techdesk.pe");
        usuario.setPassword("hashTest");
        usuario.setEstado(1);
        usuarioRepository.save(usuario);

        Optional<Usuario> porUsuario = usuarioRepository.findByUsuarioOrEmailActive("lfernandez_test");
        assertTrue(porUsuario.isPresent());
        assertEquals("Lucía", porUsuario.get().getNombres());

        Optional<Usuario> porEmail = usuarioRepository.findByUsuarioOrEmailActive("lfernandez_test@techdesk.pe");
        assertTrue(porEmail.isPresent());
        assertEquals("lfernandez_test", porEmail.get().getUsuario());
    }
}
