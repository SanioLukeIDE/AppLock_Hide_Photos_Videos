package com.applock.fingerprint.passwordlock.model;


import com.applock.fingerprint.passwordlock.R;

public enum LockStage {

    Introduction(R.string.lock_recording_intro_header, -1, true),

    HelpScreen(R.string.lock_settings_help_how_to_record, -1, false),

    ChoiceTooShort(R.string.lock_recording_incorrect_too_short, -1, true),

    FirstChoiceValid(R.string.lock_pattern_entered_header, -1, false),

    NeedToConfirm(R.string.lock_need_to_confirm, -1, true),

    ConfirmWrong(R.string.lock_need_to_unlock_wrong, -1, true),

    ChoiceConfirmed(R.string.lock_pattern_confirmed_header, -1, false);

    LockStage(int headerMessage, int footerMessage,
              boolean patternEnabled) {
        this.headerMessage = headerMessage;
        this.footerMessage = footerMessage;
        this.patternEnabled = patternEnabled;
    }

    public final int headerMessage;
    public final int footerMessage;
    public final boolean patternEnabled;
}
