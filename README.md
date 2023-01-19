# Anywhere

Anywhere is a botnet developed in Java that allows for the remote control of compromised devices. It is composed of three main components: a server, an agent and a master.

---
## Server

The server is the backbone of the Anywhere botnet, responsible for receiving commands from the master and forwarding them to the agents. It can be launched with the following arguments:
- `-allowed`: a list of IP addresses that are authorized to connect as the administrator
- `-password`: the password required to connect as the administrator
- `-port`: the port used by the server
- `-debug`: a flag that, when set to `true`, enables the display of all requests

## Master

The master is a command line interface (CLI) that allows the administrator to control the botnet. It has the following commands:
- `help`: displays a list of all available commands
- `exit`: closes the master
- `color <on/off>`: enables or disables color input
- `server <list/add/remove> [address] [port] [password]`: allows the administrator to manage the servers connected to the botnet
- `download <link>`: downloads a file from the specified link
- `stop <attack id/all>`: stops one or all ongoing attacks
- `http <address> <port> <time> [threads]`: starts a HTTP flood attack on the specified target
- `slowhttp <address> <port> <time> [threads]`: starts a slow HTTP flood attack on the specified target
- `socket <address> <port> <time> [threads]`: starts a socket flood attack on the specified target
- `udp <address> <port> <time> [threads]`: starts a UDP flood attack on the specified target

---

### Help

![alt text](https://cdn.discordapp.com/attachments/863095969436270633/1065301491474239498/image.png)

### Ping

![alt text](https://cdn.discordapp.com/attachments/863095969436270633/1065301627361300510/image.png)

### Server list

![alt text](https://cdn.discordapp.com/attachments/863095969436270633/1065678821602365500/image.png)

### Download

![alt text](https://cdn.discordapp.com/attachments/863095969436270633/1065305207497556048/image.png)

### Attack

![alt text](https://cdn.discordapp.com/attachments/863095969436270633/1065305468785930361/image.png)

## Agent

The agent is installed on the compromised devices and is responsible for executing the commands received from the server. It is persistent and has an auto-start feature. It also automatically downloads updates from one or multiple servers. The agent is able to bypass antivirus and encrypt the requests in order to remain undetected.

## Disclaimer

It is important to note that Anywhere is a tool for malicious purposes and its use is illegal. This project is for educational and research purposes only. It is strictly prohibited to use this software to infect any devices that do not belong to you. Any unauthorized use of this software is illegal and punishable by law. You are solely responsible for any actions taken using this software and must ensure that you have the appropriate legal permissions before using it. It is recommended to use this software only on your own servers or devices.

Some parts of the code are a bit old.

