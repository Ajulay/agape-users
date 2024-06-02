package com.ajulay.users.service


import com.ajulay.users.entity.User
import java.util.UUID

interface UserService {

    fun getUserById(userId: UUID): User?
    fun getAllUsers(): List<User>
    fun deleteUser(userId: UUID)
    fun createUser(user: User): User
    fun updateUser(user: User): User

}