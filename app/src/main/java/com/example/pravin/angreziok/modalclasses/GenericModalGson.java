
package com.example.pravin.angreziok.modalclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenericModalGson {

    @SerializedName("nodeType")
    @Expose
    private String nodeType;

    @SerializedName("nodeTitle")
    @Expose
    private String nodeTitle;

    @SerializedName("nodeDesc")
    @Expose
    private String nodeDesc;

    @SerializedName("resourceId")
    @Expose
    private String resourceId;

    @SerializedName("resourceText")
    @Expose
    private String resourceText;

    @SerializedName("resourceAudio")
    @Expose
    private String resourceAudio;

    @SerializedName("resourceImage")
    @Expose
    private String resourceImage;

    @SerializedName("resourceType")
    @Expose
    private String resourceType;

    @SerializedName("nodelist")
    @Expose
    private List<GenericModalGson> nodelist = null;

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeTitle() {
        return nodeTitle;
    }

    public void setNodeTitle(String nodeTitle) {
        this.nodeTitle = nodeTitle;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceText() {
        return resourceText;
    }

    public void setResourceText(String resourceText) {
        this.resourceText = resourceText;
    }

    public String getResourceAudio() {
        return resourceAudio;
    }

    public void setResourceAudio(String resourceAudio) {
        this.resourceAudio = resourceAudio;
    }

    public String getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(String resourceImage) {
        this.resourceImage = resourceImage;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<GenericModalGson> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<GenericModalGson> nodelist) {
        this.nodelist = nodelist;
    }

}
