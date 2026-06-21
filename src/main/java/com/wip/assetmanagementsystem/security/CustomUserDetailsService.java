package com.wip.assetmanagementsystem.security;

import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.repository.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_HASH = new BCryptPasswordEncoder().encode("admin123");

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (ADMIN_USERNAME.equals(username)) {
            return new User(ADMIN_USERNAME, ADMIN_HASH,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
        Employee emp = employeeRepository.findByUsername(username);
        if (emp != null && emp.getPassword() != null) {
            return new User(emp.getUsername(), emp.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + (emp.getRole() != null ? emp.getRole() : "USER"))));
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
