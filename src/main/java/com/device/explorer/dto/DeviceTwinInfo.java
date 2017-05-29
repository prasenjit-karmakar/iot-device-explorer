package com.device.explorer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Lenovo on 29-05-2017.
 */
public class DeviceTwinInfo {
    @NotEmpty
    private final String bedRoomLights;
    @NotEmpty
    private final String homeTemp;
    @NotEmpty
    private final String homeSecurityCamera;
    @NotEmpty
    private final String livingRoomLights;


    @JsonCreator
    public DeviceTwinInfo(@JsonProperty("bedRoomLights") String bedRoomLights, @JsonProperty("homeTemp") String homeTemp,
                          @JsonProperty("homeSecurityCamera") String homeSecurityCamera, @JsonProperty("livingRoomLights") String livingRoomLights) {
        this.bedRoomLights = bedRoomLights;
        this.homeTemp = homeTemp;
        this.homeSecurityCamera = homeSecurityCamera;
        this.livingRoomLights = livingRoomLights;
    }

    public String getBedRoomLights() {
        return bedRoomLights;
    }

    public String getHomeTemp() {
        return homeTemp;
    }

    public String getHomeSecurityCamera() {
        return homeSecurityCamera;
    }

    public String getLivingRoomLights() {
        return livingRoomLights;
    }
}
