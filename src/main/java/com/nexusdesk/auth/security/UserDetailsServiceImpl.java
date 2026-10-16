package com.nexusdesk.auth.security;

import com.nexusdesk.auth.entity.Usuario;
import com.nexusdesk.auth.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuarioOrEmailActive(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con credencial: " + usernameOrEmail));

        return new CustomUserDetails(usuario);
    }
}
