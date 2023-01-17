package com.example.easyratetracker2.data.models

interface ListElementModel<T> {
    val id: T
    override fun equals(other: Any?): Boolean
}