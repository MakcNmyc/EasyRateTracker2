package com.example.easyratetracker2.data.models

interface Model<T> {
    val id: T
    override fun equals(other: Any?): Boolean
}