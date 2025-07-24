package module.genome;

import java.util.Objects;

public class GenomicInterval extends Range {
	public String chromosome;

	public GenomicInterval() {
	}

    public GenomicInterval(String chromosome, int start, int end) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
    }

    public boolean overlaps(GenomicInterval other) {
        if (!this.chromosome.equals(other.chromosome)) {
            return false;
        }
        return this.start < other.end && this.end > other.start;
    }

    public int getLength() {
        return this.end - this.start;
    }

    @Override
    public String toString() {
		return chromosome + ":" + super.toString();
    }

	@Override
	public int hashCode() {
		return Objects.hash(chromosome, end, start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenomicInterval other = (GenomicInterval) obj;
		return Objects.equals(chromosome, other.chromosome) && end == other.end && start == other.start;
	}

}