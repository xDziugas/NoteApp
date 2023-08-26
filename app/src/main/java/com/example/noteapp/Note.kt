package com.example.noteapp

data class Note (
    var id: Int ?= null,
    var title: String ?= null,
    var content: String ?= null
)