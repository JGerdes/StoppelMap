package com.jonasgerdes.stoppelmap.preparation.definitions

import com.jonasgerdes.stoppelmap.dto.Locales
import com.jonasgerdes.stoppelmap.dto.data.PaymentOption
import com.jonasgerdes.stoppelmap.preparation.localizedString


val paymentOptions = mapOf(
    "paymentoption_cash" to paymentOption("bar", "cash"),
    "paymentoption_contactless" to paymentOption("kontaktlos", "contactless"),
    "paymentoption_creditcard" to paymentOption("Kreditkarte", "Creditcard"),
    "paymentoption_debitcard" to paymentOption("Debitkarte", "Debitcard"),
)


private fun paymentOption(
    de: String,
    en: String,
    icon: String? = null,
    noteDe: String? = null,
    noteEn: String? = null,
) = PaymentOption(
    name = localizedString(de = de, en = en),
    icon = icon,
    note = noteDe?.let {
        if (noteEn == null) mapOf(Locales.de to de)
        else localizedString(de = de, en = en)
    },
)