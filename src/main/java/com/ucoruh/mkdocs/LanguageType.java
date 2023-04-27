package com.ucoruh.mkdocs;

public enum LanguageType {
	
    EN_US("en-US"),
    EN_GB("en-GB"),
    EN_CA("en-CA"),
    FR_FR("fr-FR"),
    ES_ES("es-ES"),
    DE_DE("de-DE"),
    ZH_CN("zh-CN"),
    JA_JP("ja-JP"),
    RU_RU("ru-RU"),
    AR_SA("ar-SA"),
    BG_BG("bg-BG"),
    CS_CZ("cs-CZ"),
    DA_DK("da-DK"),
    EL_GR("el-GR"),
    FI_FI("fi-FI"),
    HE_IL("he-IL"),
    HI_IN("hi-IN"),
    HU_HU("hu-HU"),
    ID_ID("id-ID"),
    IT_IT("it-IT"),
    KO_KR("ko-KR"),
    NL_NL("nl-NL"),
    NO_NO("no-NO"),
    PL_PL("pl-PL"),
    PT_BR("pt-BR"),
    PT_PT("pt-PT"),
    RO_RO("ro-RO"),
    SK_SK("sk-SK"),
    SL_SI("sl-SI"),
    SV_SE("sv-SE"),
    TH_TH("th-TH"),
    TR_TR("tr-TR"),
    UK_UA("uk-UA"),
    VI_VN("vi-VN");

    private final String languageCode;

    private LanguageType(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
