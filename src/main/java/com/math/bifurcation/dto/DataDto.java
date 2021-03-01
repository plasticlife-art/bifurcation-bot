package com.math.bifurcation.dto;

/*
  @author Leonid Cheremshantsev
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "start_x",
        "r",
        "iteration_counter",
        "current_x",
        "cycle_list",
        "tfs",
        "cycle"
})
public class DataDto {

    @JsonProperty("start_x")
    private Float startX;
    @JsonProperty("r")
    private Float r;
    @JsonProperty("iteration_counter")
    private Integer iterationCounter;
    @JsonProperty("current_x")
    private Float currentX;
    @JsonProperty("cycle_list")
    private List<Float> cycleList = null;
    @JsonProperty("tfs")
    private String tfs;
    @JsonProperty("cycle")
    private List<Float> cycle = null;

    public DataDto() {

    }

    @JsonProperty("start_x")
    public Float getStartX() {
        return startX;
    }

    @JsonProperty("start_x")
    public void setStartX(Float startX) {
        this.startX = startX;
    }

    @JsonProperty("r")
    public Float getR() {
        return r;
    }

    @JsonProperty("r")
    public void setR(Float r) {
        this.r = r;
    }

    @JsonProperty("iteration_counter")
    public Integer getIterationCounter() {
        return iterationCounter;
    }

    @JsonProperty("iteration_counter")
    public void setIterationCounter(Integer iterationCounter) {
        this.iterationCounter = iterationCounter;
    }

    @JsonProperty("current_x")
    public Float getCurrentX() {
        return currentX;
    }

    @JsonProperty("current_x")
    public void setCurrentX(Float currentX) {
        this.currentX = currentX;
    }

    @JsonProperty("cycle_list")
    public List<Float> getCycleList() {
        return cycleList;
    }

    @JsonProperty("cycle_list")
    public void setCycleList(List<Float> cycleList) {
        this.cycleList = cycleList;
    }

    @JsonProperty("tfs")
    public String getTfs() {
        return tfs;
    }

    @JsonProperty("tfs")
    public void setTfs(String tfs) {
        this.tfs = tfs;
    }

    @JsonProperty("cycle")
    public List<Float> getCycle() {
        return cycle;
    }

    @JsonProperty("cycle")
    public void setCycle(List<Float> cycle) {
        this.cycle = cycle;
    }

    @Override
    public String toString() {
        return "DataDto{" +
                "startX=" + startX +
                ", r=" + r +
                ", iterationCounter=" + iterationCounter +
                ", currentX=" + currentX +
                ", cycleList=" + cycleList +
                ", tfs='" + tfs + '\'' +
                ", cycle=" + cycle +
                '}';
    }
}