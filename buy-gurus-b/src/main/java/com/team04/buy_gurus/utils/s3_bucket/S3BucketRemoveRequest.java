package com.team04.buy_gurus.utils.s3_bucket;

import lombok.Getter;

public class S3BucketRemoveRequest {
    @Getter
    public static class Filename {
        private String filename;
    }

    @Getter
    public static class Filenames {
        private String[] filename;
    }
}

