package com.learn.firebasechatapp.model

import java.util.Date

class ChatMessage {
    var messageText: String? = ""
    var messageUserId: String? = ""
    var messageUsername: String? = "[deleted]"
    var messageTime: Long? = 0

    constructor (messageUserId: String?, messageUsername: String?, messageText: String?) {
        this.messageText = messageText
        this.messageUserId = messageUserId
        this.messageUsername = messageUsername
        this.messageTime = Date().time

    }

    constructor() {}
}