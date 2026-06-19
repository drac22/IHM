package com.ihm.project.config;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ihm.project.model.Categoria;
import com.ihm.project.model.Rol;
import com.ihm.project.model.Usuario;
import com.ihm.project.model.tbl_intermedias.UsuarioRoles;
import com.ihm.project.repo.CategoriaRepository;
import com.ihm.project.repo.RolRepository;
import com.ihm.project.repo.UserRepository;

@Configuration
@Profile("dev")
public class DevDataInitializer {

    private static final List<String> ADMIN_EMAIL_ALIASES = List.of("admin@ihm.local", "admin@example.com");
    private static final List<String> TI_EMAIL_ALIASES = List.of("ti@ihm.local");
    private static final List<String> USER_EMAIL_ALIASES = List.of("user@ihm.local");

    @Bean
    CommandLineRunner seedDevData(
            RolRepository rolRepository,
            UserRepository userRepository,
            CategoriaRepository categoriaRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Rol adminRole = ensureRole(rolRepository, "ROLE_ADMIN");
            Rol userRole = ensureRole(rolRepository, "ROLE_USER");
            Rol tiRole = ensureRole(rolRepository, "ROLE_TI");

            ensureUser(
                    userRepository,
                    passwordEncoder,
                    "admin@gmail.com",
                    "admin",
                    "Admin",
                    "IHM",
                    "999111222",
                    adminRole,
                    ADMIN_EMAIL_ALIASES);

            ensureUser(
                    userRepository,
                    passwordEncoder,
                    "tecnico@gmail.com",
                    "tecnico",
                    "Soporte",
                    "Tecnico",
                    "999111333",
                    tiRole,
                    TI_EMAIL_ALIASES);

            ensureUser(
                    userRepository,
                    passwordEncoder,
                    "usuario@gmail.com",
                    "usuario",
                    "Usuario",
                    "Final",
                    "999111444",
                    userRole,
                    USER_EMAIL_ALIASES);

            ensureCategoria(categoriaRepository, "Hardware");
            ensureCategoria(categoriaRepository, "Software");
            ensureCategoria(categoriaRepository, "Redes");
            ensureCategoria(categoriaRepository, "Accesos");
        };
    }

    private Rol ensureRole(RolRepository rolRepository, String roleName) {
        return rolRepository.findByName(roleName)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setName(roleName);
                    return rolRepository.save(rol);
                });
    }

    private void ensureUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String email,
            String username,
            String nombre,
            String apellido,
            String celular,
            Rol role,
            List<String> legacyEmails) {
        Usuario usuario = findSeedUser(userRepository, email, legacyEmails)
                .orElseGet(Usuario::new);

        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(username));
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCelular(celular);
        usuario.setRoles(resolveRoles(usuario, role));

        userRepository.save(usuario);
    }

    private Optional<Usuario> findSeedUser(
            UserRepository userRepository,
            String targetEmail,
            List<String> legacyEmails) {
        Optional<Usuario> existingUser = userRepository.findByEmail(targetEmail);
        if (existingUser.isPresent()) {
            return existingUser;
        }

        for (String legacyEmail : legacyEmails) {
            Optional<Usuario> legacyUser = userRepository.findByEmail(legacyEmail);
            if (legacyUser.isPresent()) {
                return legacyUser;
            }
        }

        return Optional.empty();
    }

    private UsuarioRoles buildUserRole(Usuario usuario, Rol role) {
        UsuarioRoles usuarioRol = new UsuarioRoles();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(role);
        return usuarioRol;
    }

    private List<UsuarioRoles> resolveRoles(Usuario usuario, Rol role) {
        List<UsuarioRoles> currentRoles = usuario.getRoles();
        if (currentRoles != null && !currentRoles.isEmpty()) {
            boolean alreadyAssigned = currentRoles.stream()
                    .peek(usuarioRol -> usuarioRol.setUsuario(usuario))
                    .anyMatch(usuarioRol -> usuarioRol.getRol().getName().equals(role.getName()));
            if (alreadyAssigned) {
                return currentRoles;
            }
        }

        return List.of(buildUserRole(usuario, role));
    }

    private void ensureCategoria(CategoriaRepository categoriaRepository, String nombre) {
        categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Categoria categoria = new Categoria();
                    categoria.setNombre(nombre);
                    return categoriaRepository.save(categoria);
                });
    }
}
