/**
 * This class represents the Usuario entity in the blog_pessoal application.
 * It is part of the model package and is used to define the structure and
 * behavior of user-related data within the system.
 */
package com.montreal.acelera.blog_pessoal.model;

import java.util.Collection;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "tb_usuarios")
public class Usuario implements UserDetails {
	static public final int LENGTH = 255;
	static public final int FOTO_LENGTH = 5000;
	static public final String DEFAULT_PIC_PATH = "/images/DefaultProfilePic.png";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = LENGTH)
	@Column(nullable = false)
	private String nome;

	@NotBlank
	@Size(max = LENGTH)
	@Column(nullable = false, unique = true)
	private String usuario;

	@NotBlank
	@Size(min = 8, max = LENGTH)
	@Column(nullable = false)
	private String senha;
	
	@Size(max = FOTO_LENGTH)
	@Builder.Default
	private String foto = DEFAULT_PIC_PATH;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Postagem> postagens;

    private boolean locked;

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuario));
    }

    @Override
    public String getUsername() {
        return usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		return senha;
	}
}
