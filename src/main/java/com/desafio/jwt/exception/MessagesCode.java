package com.desafio.jwt.exception;

public enum MessagesCode {
    EMPTY_TOKEN("jwt.error.empty-token"),
    INVALID_FORMAT("jwt.error.invalid-format"),
    INVALID_CLAIM_COUNT("jwt.error.invalid-claim-count"),
    MISSING_CLAIMS("jwt.error.missing-claims"),
    NAME_TOO_LONG("jwt.error.name-too-long"),
    NAME_HAS_DIGITS("jwt.error.name-has-digits"),
    INVALID_ROLE("jwt.error.invalid-role"),
    SEED_NOT_INTEGER("jwt.error.seed-not-integer"),
    SEED_NOT_PRIME("jwt.error.seed-not-prime");

    private final String messageKey;

    MessagesCode(String messageKey) {
        this.messageKey = messageKey;
    }

    public String key() {
        return messageKey;
    }
}
