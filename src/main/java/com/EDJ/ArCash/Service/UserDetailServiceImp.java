package com.EDJ.ArCash.Service;


import com.EDJ.ArCash.Models.User;
import com.EDJ.ArCash.Repository.UserRepository;
import com.EDJ.ArCash.Security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


//   @Override
//   public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
//       try {
//           Long id = Long.parseLong(idString);
//           User user = userRepository.findById(id)
//                   .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
//           return new CustomUserDetails(user);
//       } catch (NumberFormatException e) {
//           throw new UsernameNotFoundException("Id inválido");
//       }
//   }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long id = Long.parseLong(username);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }



}
