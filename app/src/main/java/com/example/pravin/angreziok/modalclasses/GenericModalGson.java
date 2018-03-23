
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
    private Object nodeDesc;

    @SerializedName("resourceId")
    @Expose
    private Object resourceId;

    @SerializedName("resourceText")
    @Expose
    private Object resourceText;

    @SerializedName("resourceAudio")
    @Expose
    private Object resourceAudio;

    @SerializedName("resourceImage")
    @Expose
    private Object resourceImage;

    @SerializedName("resourceType")
    @Expose
    private Object resourceType;

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

    public Object getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(Object nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public Object getResourceId() {
        return resourceId;
    }

    public void setResourceId(Object resourceId) {
        this.resourceId = resourceId;
    }

    public Object getResourceText() {
        return resourceText;
    }

    public void setResourceText(Object resourceText) {
        this.resourceText = resourceText;
    }

    public Object getResourceAudio() {
        return resourceAudio;
    }

    public void setResourceAudio(Object resourceAudio) {
        this.resourceAudio = resourceAudio;
    }

    public Object getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(Object resourceImage) {
        this.resourceImage = resourceImage;
    }

    public Object getResourceType() {
        return resourceType;
    }

    public void setResourceType(Object resourceType) {
        this.resourceType = resourceType;
    }

    public List<GenericModalGson> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<GenericModalGson> nodelist) {
        this.nodelist = nodelist;
    }

}
