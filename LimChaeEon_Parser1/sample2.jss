//sample2.jss is invalid 
<script_start>
var 9temperature = 20;
var limit = 40, fan = 0;
while (temperature <= limit) {
    if (temperature == limit) { 
        document.writeln("temperature limit"); 
        temperature = 20;
        fan = 1;
    } 
    elsa {
        temperature++;
        fan = 0; 
    }
} 
<script_end>