package com.crossover.trial.weather;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    private double mean;
    private int first;
    private int second;
    private int third;
    private int count;

    /** private constructor, use the builder to create this object */
	private DataPoint() {

    }
    
    protected DataPoint(Builder builder) {
        this.first = builder.first;
        this.mean = builder.mean;
        this.second = builder.median;
        this.third = builder.last;
        this.count = builder.count;
    }
    
    public void setMean(double mean) {
		this.mean = mean;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public void setThird(int third) {
		this.third = third;
	}

	public void setCount(int count) {
		this.count = count;
	}

    /** the mean of the observations */
    public double getMean() {
        return mean;
    }

    /** 1st quartile -- useful as a lower bound */
    public int getFirst() {
        return first;
    }

    /** 2nd quartile -- median value */
    public int getSecond() {
        return second;
    }
    
    /** 3rd quartile value -- less noisy upper value */
    public int getThird() {
        return third;
    }

    /** the total number of measurements */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }
    
    @Override
    public int hashCode() {
    	return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class Builder {
        private int first;
        private int mean;
        private int median;
        private int last;
        private int count;

        public Builder withFirst(int first) {
            this.first= first;
            return this;
        }

        public Builder withMean(int mean) {
            this.mean = mean;
            return this;
        }

        public Builder withMedian(int median) {
            this.median = median;
            return this;
        }

        public Builder withCount(int count) {
            this.count = count;
            return this;
        }

        public Builder withLast(int last) {
            this.last = last;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this);
        }
    }
}
