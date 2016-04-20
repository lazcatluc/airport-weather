package com.crossover.trial.weather.airport;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

public class AirportLoaderTest {

    @Test
    public void stripsQuotesFromBeginningAndEnd() {
        assertThat(AirportLoader.stripQuotes("\"original\"")).isEqualTo("original");
    }
    
    @Test
    public void doesntStripsQuotesFromBeginningOnly() {
        assertThat(AirportLoader.stripQuotes("\"original")).isEqualTo("\"original");
    }
    
    @Test
    public void doesntStripsQuotesFromEndOnly() {
        assertThat(AirportLoader.stripQuotes("original\"")).isEqualTo("original\"");
    }

    @Test
    public void returnsUnquotedOriginal() {
        assertThat(AirportLoader.stripQuotes("original")).isEqualTo("original");
    }
    
    @Test
    public void getsIataFromStringLine() throws Exception {
        String[] line = new String[] {"1","General Edward Lawrence Logan Intl","Boston","United States","BOS","KBOS","42.364347","-71.005181","19","-5","A"};
        
        assertThat(AirportLoader.buildAddAirportRequestPath(line)).isEqualTo("/airport/BOS/42.364347/-71.005181");
    }
    
    @Test
    public void usesIdAsIataWhenIataIsMissing() throws Exception {
        String[] line = new String[] {"1","General Edward Lawrence Logan Intl","Boston","United States","","KBOS","42.364347","-71.005181","19","-5","A"};
        
        assertThat(AirportLoader.buildAddAirportRequestPath(line)).isEqualTo("/airport/UNKNOWN_0001/42.364347/-71.005181");
    }
    
    @Test(expected = FileNotFoundException.class)
    public void throwsExceptionIfAirportFileDoesntExist() throws Exception {
        AirportLoader.main(new String[] {"non-existent.file"});
    }
    
    @Test(expected = FileNotFoundException.class)
    public void throwsExceptionIfAirportFileExistsButIsEmpty() throws Exception {
        String dummyPath = ClassLoader.getSystemClassLoader().getResource("dummy.dat").getPath();
        assertThat(new File(dummyPath).exists()).isTrue();
        
        AirportLoader.main(new String[] {dummyPath});
    }
}
