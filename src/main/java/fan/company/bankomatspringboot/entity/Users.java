package fan.company.bankomatspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createAt; //user qachon yaratilganligini bildiradi

    @UpdateTimestamp
    private Timestamp updateAt;  //user qachon yangilanganligini bildiradi

    // @ManyToOne(fetch = FetchType.EAGER)  bunday qilsa Lazy yuklanganda kerak
    @ManyToOne
    private Role roles;     //userning rollari

    private boolean accountNonExpired = true; // userning amal qilish muddati o'tmagan

    private boolean accountNonLocked = true ; //user blocklanmaganligini bildiradi

    private boolean credentialsNonExpired = true;

    private boolean enabled = true; //user yoniqmi



    /**-------------Userdatailsni metodlari------------------**/


    //Userning huquqlari ro'yxati
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.roles);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    //amal qilish muddati tugamaganligi
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    //Accaunt bloklanganligi holatini qaytaradi
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    //Accauntning ishonchlimi yoki yoqmi shuni qaytaradi
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    //Accauntning activligi
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Users(String fullName, String email, String password, Role roles) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

}
