package com.ramadan.notify.data.model

import  java.io.Serializable;

class WrittenNote(
    var ID: String, var date: String,
    var name: String, var content: String,
    var noteColor: Int
) : Serializable