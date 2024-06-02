package com.ajulay.users.service.impl

import com.ajulay.users.entity.User
import com.ajulay.users.repository.UserRepository
import com.ajulay.users.service.UserService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun getUserById(userId: UUID): User? =
         userRepository.findById(userId).orElse(null)

    override fun createUser(user: User): User {
        return userRepository.save(user)
    }


    override fun updateUser(user: User): User {
        if (!userRepository.existsById(user.userId)) {
            throw IllegalArgumentException("User with ID ${user.userId} does not exist")
        }
        return userRepository.save(user)
    }

    override fun deleteUser(userId: UUID) {
        if (!userRepository.existsById(userId)) {
            throw IllegalArgumentException("User with ID $userId does not exist")
        }
        userRepository.deleteById(userId)
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

}