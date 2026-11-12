package com.ivan.playlistmaker.sharing.domain.api

import com.ivan.playlistmaker.settings.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openLink(link: String)
    fun openEmail(emailData: EmailData)
}
