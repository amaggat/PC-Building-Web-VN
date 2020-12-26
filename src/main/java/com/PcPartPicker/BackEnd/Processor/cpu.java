package com.PcPartPicker.BackEnd.Processor;


import com.PcPartPicker.BackEnd._Model.PcProfile;
import com.PcPartPicker.BackEnd._Model.electronicComponents;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Entity
@Table(name ="cpu")
public class cpu extends electronicComponents {

    @Column(name = "socket")
    @NotEmpty
    private String socket = new String();

//    private double BaseClock;
//    private double BoostCLock;

    @Column(name = "Cores")
    @NotEmpty
    private int Cores;
    
    @Column(name = "Threads")
    @NotEmpty
    private int Threads;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cpu", fetch = FetchType.EAGER)
    private List<cpuPriceList> PriceList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cpu")
    private List<PcProfile> pcProfile;

    public String getSocket() {
        return socket;
    }

    public int getCores() {
        return Cores;
    }

    public int getThreads() {
        return Threads;
    }

//    public double getBaseClock() {
//        return BaseClock;
//    }
//
//    public double getBoostCLock() {
//        return BoostCLock;
//    }
//
//    public void setBaseClock(double baseClock) {
//        BaseClock = baseClock;
//    }
//
//    public void setBoostCLock(double boostCLock) {
//        BoostCLock = boostCLock;
//    }




    public void setCores(int cores) {
        Cores = cores;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public void setThreads(int threads) {
        Threads = threads;
    }

    public List<com.PcPartPicker.BackEnd.Processor.cpuPriceList> getPriceList() {
        return PriceList;
    }

    public void setPriceList(List<com.PcPartPicker.BackEnd.Processor.cpuPriceList> cpuPriceList) {
        this.PriceList = cpuPriceList;
    }

//    public List<PcProfile> getPcProfile() {
//        return pcProfile;
//    }
//
//    public void setPcProfile(List<PcProfile> pcProfile) {
//        this.pcProfile = pcProfile;
//    }
}
