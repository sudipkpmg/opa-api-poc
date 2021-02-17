package gov.tn.dhs.opa.model;

public class ExampleSummary {

    private double casesRead;
    private double casesProcessed;
    private double casesIgnored;
    private double processorDurationSec;
    private double processorCasesPerSec;

    public double getCasesRead() {
        return casesRead;
    }

    public void setCasesRead(double casesRead) {
        this.casesRead = casesRead;
    }

    public double getCasesProcessed() {
        return casesProcessed;
    }

    public void setCasesProcessed(double casesProcessed) {
        this.casesProcessed = casesProcessed;
    }

    public double getCasesIgnored() {
        return casesIgnored;
    }

    public void setCasesIgnored(double casesIgnored) {
        this.casesIgnored = casesIgnored;
    }

    public double getProcessorDurationSec() {
        return processorDurationSec;
    }

    public void setProcessorDurationSec(double processorDurationSec) {
        this.processorDurationSec = processorDurationSec;
    }

    public double getProcessorCasesPerSec() {
        return processorCasesPerSec;
    }

    public void setProcessorCasesPerSec(double processorCasesPerSec) {
        this.processorCasesPerSec = processorCasesPerSec;
    }

}
