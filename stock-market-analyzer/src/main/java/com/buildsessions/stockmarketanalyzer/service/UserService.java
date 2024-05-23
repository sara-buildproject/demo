package com.buildsessions.stockmarketanalyzer.service;

import com.buildsessions.stockmarketanalyzer.entity.CustomUser;
import com.buildsessions.stockmarketanalyzer.entity.Stock;
import com.buildsessions.stockmarketanalyzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StockService stockService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<CustomUser> maybeUser = userRepository.findCustomUserByUsername(username);
        if (maybeUser.isPresent()) {
            CustomUser user = maybeUser.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(getRoles(user))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public CustomUser addUser(CustomUser user) {
        return userRepository.save(user);
    }

    public CustomUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Set<Long> getMonitoredStockIdsForUser(Long userId) {
        CustomUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getMonitoredStockIds();
        }
        return null;
    }

    public void addStockToUser(Long userId, String symbol) {
        CustomUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Stock stock = stockService.getOrCreateStockBySymbol(symbol);
            Set<Long> monitoredStockIds = user.getMonitoredStockIds();
            monitoredStockIds.add(stock.getId());
            user.setMonitoredStockIds(monitoredStockIds);
            userRepository.save(user);
        }
    }

    public void removeStockFromUser(Long userId, String symbol) {
        CustomUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Stock stock = stockService.getStockBySymbol(symbol);
            Set<Long> monitoredStockIds = user.getMonitoredStockIds();
            monitoredStockIds.remove(stock.getId());
            user.setMonitoredStockIds(monitoredStockIds);
            userRepository.save(user);
            stockService.checkAndRemoveStockIfNotMonitored(stock);
        }
    }

    private String[] getRoles(CustomUser user) {
        if(user.getRoles() == null) {
            return new String[]{"USER"};
        }
        return user.getRoles().split(",");
    }
}
