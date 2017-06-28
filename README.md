#  TCP and UDP Client Server Application


## Compiling:
1. After compiling the java files using the javac command, run the server using:
	 ```
	java smsengineTCP <port Number> <suspicious words text file>
	    For eg: java smsengineTCP 4000 suspicious-words.txt
	```
	
1. run the client using:
	```
	java smsclientTCP <IP address> <port Number> <message text file>
	    For eg: java smsclientTCP 127.0.0.1 4000 msg.txt
	```

1. To run the UDP version use same commands with appropriate files.

## Limitation:

If the suspicious words in the suspicious-words.txt files are separated by commas, then when comparing them to the received message, the words followed by comma in the suspicious-words.txt will not be compared. This can be fixed by using regular expression to remove the commas, but due to lack of time I wasn't able to do it.
