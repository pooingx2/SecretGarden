#include<stdio.h>
#include<string.h>
#include<stdlib.h>

#include<libxml/xmlmemory.h>
#include<libxml/parser.h>
#include<libxml/encoding.h>
#include<libxml/xmlwriter.h>


int getNodeInfo(xmlNodePtr cur, char* nodeInfo);
int det_nodeCategory(xmlNodePtr cur);

int travarseDFS(xmlDocPtr doc, xmlNodePtr cur);
int travarseBFS(xmlDocPtr doc, xmlNodePtr cur);

int setAttributes(xmlNodePtr cur, char *name, char *parent, char *depth);
int createBurket(xmlNodePtr cur, int category, char *name, char *parent, char *depth);

xmlNodePtr searchNode(xmlChar nodename);
xmlNodePtr getdocRoot(xmlDocPtr doc);

char* showChildNodes(xmlDocPtr doc, xmlNodePtr cur, char *buf);
int parseDoc(char *docname, xmlDocPtr *doc);


