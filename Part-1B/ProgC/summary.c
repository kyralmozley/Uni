//
//  summary.c
//
//
//  Created by Kyra Mozley on 02/01/2019.
//
/*************************************************************
 Part 2 - read in IP / TCP header info from Log file
 Input: log file
 Output:
 1. IP Source Address
 2. IP Destination Address
 3. First packets IP Header size field (IHL)
 4. Length of first IP Packet (len)
 5. Value of first TCP Packet header size (data offset)
 6. Total number of IP packets in trace
 **************************************************************/


#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <netinet/in.h>

#define BUFFER_SIZE 20

typedef struct ip_header {
    uint8_t hlenver;    // Version & Internet Header Length
    uint8_t tos;        // Type of Service
    uint16_t len;       // Total Length
    uint16_t id;        // Identification
    uint16_t off;       // Flags & Fragment Offset
    uint8_t ttl;        // Time to Live
    uint8_t p;          // Protocol
    uint16_t sum;       // Checksum
    uint32_t src;       // Source Address
    uint32_t dst;       // Destination Address
} ip_header;

#define IP_HLEN(lenver) (lenver & 0x0f)
#define IP_VER(lenver) (lenver >> 4)

typedef struct tcp_header {
    uint16_t sport;       // Source Port
    uint16_t dport;       // Destination Port
    uint32_t seqnum;    // Sequence Number
    uint32_t acknum;    // Acknowledgment Number
    uint8_t doff;        // Data Offset & Resevered
    uint8_t ctrl;       // Control flags
    uint16_t win;       // Window
    uint16_t chksum;    // Checksum
    uint16_t urgptr;    // Urgent Pointer
} tcp_header;

#define TCP_OFF(offres) (offres >> 4)

//functions for formaating
uint8_t read_8(char **pos) {
    uint8_t ret = (uint8_t)(**pos);
    (*pos)++;
    return ret;
}

uint16_t read_16(char **pos) {
    uint16_t ret = 0;
    ret = ((uint16_t)read_8(pos)) << 8;
    ret |= (uint16_t)read_8(pos);
    return ret;
}

uint32_t read_32(char **pos) {
    uint32_t ret = 0;
    ret = ((uint32_t)read_8(pos)) << 24;
    ret |= ((uint32_t)read_8(pos)) << 16;
    ret |= ((uint32_t)read_8(pos)) << 8;
    ret |= ((uint32_t)read_8(pos));
    return ret;
}

char *get_ip_address(uint32_t addr) {
    char *ret = (char*)malloc(16 * sizeof(char));
    int val1 = (addr >> 24) & 0xFF;
    int val2 = (addr >> 16) & 0xFF;
    int val3 = (addr >> 8) & 0xFF;
    int val4 = (addr & 0xFF);
    sprintf(ret, "%d.%d.%d.%d", val1, val2, val3, val4);
    return ret;
}

void read_ip_header(char *buf, ip_header *ip) {
    ip->hlenver = read_8(&buf);
    ip->tos = read_8(&buf);
    ip->len = read_16(&buf);
    ip->id = read_16(&buf);
    ip->off = read_16(&buf);
    ip->ttl = read_8(&buf);
    ip->p = read_8(&buf);
    ip->sum = read_16(&buf);
    ip->src = read_32(&buf);
    ip->dst = read_32(&buf);
}

void read_tcp_header(char *buf, tcp_header *tcp) {
    tcp->sport = read_16(&buf);
    tcp->dport = read_16(&buf);
    tcp->seqnum = read_32(&buf);
    tcp->acknum = read_32(&buf);
    tcp->doff = read_8(&buf);
    tcp->ctrl = read_8(&buf);
    tcp->win = read_16(&buf);
    tcp->chksum = read_16(&buf);
    tcp->urgptr = read_16(&buf);
}

int main(int argc, char **argv) {
    FILE *logfile;
    
    char *source;
    char *destination;
    
    int first_ihl;
    int first_len;
    int first_offset;
    int total_ip_packets = 0;
    
    char buffer[BUFFER_SIZE];
    
    if (argc != 2) {
        fprintf(stdout, "Usage: <file>\n");
        return 1;
    }
    
    if ((logfile = fopen(argv[1], "rb")) == 0) {
        fprintf(stdout, "Unable to open the log file\n");
        return 2;
    }
    
    while (!feof(logfile)) {
        //allocate space
        ip_header *this_ip = (ip_header*)malloc(sizeof(ip_header));
        tcp_header *this_tcp = (tcp_header*)malloc(sizeof(tcp_header));
        
        char *cptr = buffer;
        
        fread(buffer, sizeof(char), BUFFER_SIZE, logfile);
        if (feof(logfile)) break;
        read_ip_header(cptr, this_ip);
        
        int ihl = IP_HLEN(this_ip->hlenver);
        int totlen = (this_ip->len);
        
        if (!total_ip_packets) {
            //Client initalises connection by sending packet
            //so source of first packet is actually destination
            source = get_ip_address(this_ip->dst);
            destination = get_ip_address(this_ip->src);
            first_ihl = ihl;
            first_len = totlen;
        }
        
        int rem = (4*ihl - BUFFER_SIZE);    // remaining bytes for the header
        fseek(logfile, rem, SEEK_CUR);  // skip the remainder of the header
        
        fread(buffer, sizeof(char), BUFFER_SIZE, logfile);
        cptr = buffer;
        read_tcp_header(cptr, this_tcp);
        
        int offset = TCP_OFF(this_tcp->doff);
        
        if (!total_ip_packets) {
            first_offset = offset;
        }
        
        int rem2 = totlen - (2*BUFFER_SIZE + rem);
        fseek(logfile, rem2, SEEK_CUR);
        
        total_ip_packets++;
    }
    
    fprintf(stdout, "%s %s %d %d %d %d\n", source, destination, first_ihl, first_len, first_offset, total_ip_packets);
    
    fclose(logfile);
    
    return 0;
}
