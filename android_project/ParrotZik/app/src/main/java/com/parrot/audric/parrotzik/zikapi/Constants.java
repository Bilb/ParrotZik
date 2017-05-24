package com.parrot.audric.parrotzik.zikapi;

/**
 * Created by audric on 13/05/17.
 */

public class Constants {

    /*********
     * Audio *
     *********/
    // Concert hall
    public static final String SoundEffectGet         = "/api/audio/sound_effect/get";
    public static final String SoundEffectEnabledGet  = "/api/audio/sound_effect/enabled/get";
    public static final String SoundEffectEnabledSet  = "/api/audio/sound_effect/enabled/set";
    public static final String SoundEffectAngleGet    = "/api/audio/sound_effect/angle/get";
    public static final String SoundEffectAngleSet    = "/api/audio/sound_effect/angle/set";
    public static final String SoundEffectRoomSizeGet = "/api/audio/sound_effect/room_size/get";
    public static final String SoundEffectRoomSizeSet = "/api/audio/sound_effect/room_size/set";

    // Equalizer
    public static final String EqualizerGet            = "/api/audio/equalizer/get";
    public static final String EqualizerEnabledGet     = "/api/audio/equalizer/enabled/get";
    public static final String EqualizerEnabledSet     = "/api/audio/equalizer/enabled/set";
    public static final String EqualizerPresetsListGet = "/api/audio/equalizer/presets_list/get";
    public static final String EqualizerPresetIdGet    = "/api/audio/equalizer/preset_id/get";
    public static final String EqualizerPresetIdSet    = "/api/audio/equalizer/preset_id/set";
    public static final String EqualizerPresetValueGet = "/api/audio/equalizer/preset_value/get";
    public static final String EqualizerPresetValueSet = "/api/audio/equalizer/preset_value/set";

    // Active noise control (noise cancellation)
    public static final String ANCEnableGet = "/api/audio/noise_cancellation/enabled/get";
    public static final String ANCEnableSet = "/api/audio/noise_cancellation/enabled/set";

    public static final String SpecificModeEnableGet = "/api/audio/specific_mode/enabled/get";
    public static final String SpecificModeEnableSet = "/api/audio/specific_mode/enabled/set";


    /*************
     * ZikBluetoothHelper *
     *************/

    public static final String FriendlynameGet = "/api/bluetooth/friendlyname/get";
    public static final String FriendlynameSet = "/api/bluetooth/friendlyname/set";


    /**********
     * System *
     **********/

    public static final String BatteryGet      = "/api/system/battery/get";
    public static final String BatteryLevelGet = "/api/system/battery_level/get";

    public static final String ANCPhoneModeEnableGet = "/api/system/anc_phone_mode/enabled/get";
    public static final String ANCPhoneModeEnableSet = "/api/system/anc_phone_mode/enabled/set";

    public static final String AutoConnectionEnableGet = "/api/system/auto_connection/enabled/get";
    public static final String AutoConnectionEnableSet = "/api/system/auto_connection/enabled/set";

    public static final String AutoPowerOffGet = "/api/system/auto_power_off/get";
    public static final String AutoPowerOffSet = "/api/system/auto_power_off/set";

    public static final String AutoPowerOffPresetsListGet = "/api/system/auto_power_off/presets_list_get";

    public static final String Calibration = "/api/system/calibrate";

    public static final String HeadDetectionEnabledGet = "/api/system/head_detection/enabled/get";
    public static final String HeadDetectionEnabledSet = "/api/system/head_detection/enabled/set";

    public static final String DeviceTypeGet = "/api/system/device_type/get";


    /*********
     * Other *
     *********/

    public static final String AppliVersionSet       = "/api/appli_version/set";
    public static final String DownloadCheckStateGet = "/api/software/download_check_state/get";
    public static final String DownloadSizeSet       = "/api/software/download_size/set";
    public static final String VersionCheckingGet    = "/api/software/version_checking/get";
    public static final String VersionGet            = "/api/software/version/get";
}
