package com.eatzip.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactInformation {

    private String email;

    private String phoneNo;

    private String twitterUrl;

    private String instagramUrl;

    private String youtubeUrl;
}
