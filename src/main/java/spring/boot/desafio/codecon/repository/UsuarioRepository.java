package spring.boot.desafio.codecon.repository;

import org.springframework.stereotype.Component;
import spring.boot.desafio.codecon.model.Usuario;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UsuarioRepository {

    private final Map<UUID, Usuario> db = new ConcurrentHashMap<>();

    public void  saveAll(Collection<Usuario> usuarios){
        usuarios.forEach(usuario -> {
            db.put((usuario.getId()), usuario);
        });
    }

    public Collection<Usuario> findAll(){
        return db.values();
    }

}
