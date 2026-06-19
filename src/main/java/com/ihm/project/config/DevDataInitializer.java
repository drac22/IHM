package com.ihm.project.config;

import java.util.List;

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
                    "admin@ihm.local",
                    "Admin123*",
                    "Admin",
                    "IHM",
                    "999111222",
                    adminRole);

            ensureUser(
                    userRepository,
                    passwordEncoder,
                    "ti@ihm.local",
                    "Ti123456*",
                    "Soporte",
                    "Tecnico",
                    "999111333",
                    tiRole);

            ensureUser(
                    userRepository,
                    passwordEncoder,
                    "user@ihm.local",
                    "User1234*",
                    "Usuario",
                    "Final",
                    "999111444",
                    userRole);

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
            String rawPassword,
            String nombre,
            String apellido,
            String celular,
            Rol role) {
        if (userRepository.existsByEmail(email)) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(rawPassword));
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCelular(celular);

        UsuarioRoles usuarioRol = new UsuarioRoles();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(role);
        usuario.setRoles(List.of(usuarioRol));

        userRepository.save(usuario);
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
