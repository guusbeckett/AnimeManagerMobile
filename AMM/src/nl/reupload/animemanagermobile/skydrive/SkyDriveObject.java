/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
//------------------------------------------------------------------------------
// Copyright (c) 2012 Microsoft Corporation. All rights reserved.
//------------------------------------------------------------------------------

package nl.reupload.animemanagermobile.skydrive;

import org.json.JSONObject;

public abstract class SkyDriveObject {
    public interface Visitor {
        public void visit(SkyDriveAlbum album);
        public void visit(SkyDriveAudio audio);
        public void visit(SkyDrivePhoto photo);
        public void visit(SkyDriveFolder folder);
        public void visit(SkyDriveFile file);
        public void visit(SkyDriveVideo video);
    }

    public static class From {
        private final JSONObject mFrom;

        public From(JSONObject from) {
            assert from != null;
            mFrom = from;
        }

        public String getName() {
            return mFrom.optString("name");
        }

        public String getId() {
            return mFrom.optString("id");
        }

        public JSONObject toJson() {
            return mFrom;
        }
    }

    public static class SharedWith {
        private final JSONObject mSharedWidth;

        public SharedWith(JSONObject sharedWith) {
            assert sharedWith != null;
            mSharedWidth = sharedWith;
        }

        public String getAccess() {
            return mSharedWidth.optString("access");
        }

        public JSONObject toJson() {
            return mSharedWidth;
        }
    }


    public static SkyDriveObject create(JSONObject skyDriveObject) {
        String type = skyDriveObject.optString("type");

        if (type.equals(SkyDriveFolder.TYPE)) {
            return new SkyDriveFolder(skyDriveObject);
        } else if (type.equals(SkyDriveFile.TYPE)) {
            return new SkyDriveFile(skyDriveObject);
        } else if (type.equals(SkyDriveAlbum.TYPE)) {
            return new SkyDriveAlbum(skyDriveObject);
        } else if (type.equals(SkyDrivePhoto.TYPE)) {
            return new SkyDrivePhoto(skyDriveObject);
        } else if (type.equals(SkyDriveVideo.TYPE)) {
            return new SkyDriveVideo(skyDriveObject);
        } else if (type.equals(SkyDriveAudio.TYPE)) {
            return new SkyDriveAudio(skyDriveObject);
        }

        throw new AssertionError("Unknown SkyDriveObject type");
    }

    protected final JSONObject mObject;

    public SkyDriveObject(JSONObject object) {
        assert object != null;
        mObject = object;
    }

    public abstract void accept(Visitor visitor);

    public String getId() {
        return mObject.optString("id");
    }

    public From getFrom() {
        return new From(mObject.optJSONObject("from"));
    }

    public String getName() {
        return mObject.optString("name");
    }

    public String getParentId() {
        return mObject.optString("parent_id");
    }

    public String getDescription() {
        return mObject.isNull("description") ? null : mObject.optString("description");
    }

    public String getType() {
        return mObject.optString("type");
    }

    public String getLink() {
        return mObject.optString("link");
    }

    public String getCreatedTime() {
        return mObject.optString("created_time");
    }

    public String getUpdatedTime() {
        return mObject.optString("updated_time");
    }

    public String getUploadLocation() {
        return mObject.optString("upload_location");
    }

    public SharedWith getSharedWith() {
        return new SharedWith(mObject.optJSONObject("shared_with"));
    }

    public JSONObject toJson() {
        return mObject;
    }


}
