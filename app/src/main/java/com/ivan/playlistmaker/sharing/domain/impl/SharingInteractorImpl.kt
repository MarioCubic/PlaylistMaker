package com.ivan.playlistmaker.sharing.domain.impl

import com.ivan.playlistmaker.settings.domain.model.EmailData
import com.ivan.playlistmaker.sharing.domain.api.ExternalNavigator
import com.ivan.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val shareAppString: String,
    private val termsLinkString: String,
    private val emailString: String,
    private val emailSubjectString: String,
    private val emailMessageString: String
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(shareAppString)
    }

    override fun openTerms() {
        externalNavigator.openLink(termsLinkString)
    }

    override fun openSupport() {
        externalNavigator.openEmail(
            EmailData(
                email = emailString,
                subject = emailSubjectString,
                message = emailMessageString
            )
        )
    }
}
