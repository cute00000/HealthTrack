package com.healthtrack.service;

import com.healthtrack.entity.Doctor;
import com.healthtrack.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService implements UserDetailsService {
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Doctor createDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }
    
    public Optional<Doctor> findByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }
    
    public boolean existsByUsername(String username) {
        return doctorRepository.existsByUsername(username);
    }
    
    public boolean existsByLicenseId(Long licenseId) {
        return doctorRepository.existsByLicenseId(licenseId);
    }
    
    public boolean existsByPhone(String phone) {
        return doctorRepository.existsByPhone(phone);
    }
    
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
    
    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctor = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor Not Found with username: " + username));
        return doctor;
    }
}