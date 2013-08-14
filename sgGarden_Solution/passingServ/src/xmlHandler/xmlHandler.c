#include "xmlHandler.h"





int getNodeInfo(xmlNodePtr cur, char *nodeInfo)
{
	xmlChar *id;
	//Check Parameter
	if(cur == NULL)
		printf("node is null\n");

	id = xmlGetProp(cur, (const xmlChar *) "id");
	
	//printf("%s %s ", cur->name, id);
	//printf("sprintf START \n");

	//sprintf(nodeInfo, "%s", cur->name);
	//sprintf(nodeInfo, ",");
	//sprintf(nodeInfo, "%s", id);
	//sprintf(nodeInfo, "END");
	strcat(nodeInfo, "[");
	strcat(nodeInfo, cur->name);
	strcat(nodeInfo, ",");
	strcat(nodeInfo, id);
	strcat(nodeInfo, "]");

	//printf("sprintf END \n");
	//printf("node Info is %s \n", nodeInfo);
	
	return 1;
}

int det_nodeCategory(xmlNodePtr cur)
{

	//Check Parameter
	if(cur == NULL)
		printf("node is null\n");

	if((!xmlStrcmp(cur->name, (const xmlChar *) "folder")))
		return 0;
	else if((!xmlStrcmp(cur->name, (const xmlChar *) "file" )))
		return 1;
	else
		return 2;
		
}

int travarseDFS(xmlDocPtr doc, xmlNodePtr cur)
{
	
}
	
int travarseBFS(xmlDocPtr doc, xmlNodePtr cur)
{
	
}

xmlNodePtr searchNode(xmlChar nodename)
{
	
}

int setAttributes(xmlNodePtr cur, char *name, char *parent, char *depth)
{
	xmlAttrPtr 	att_name;
	xmlAttrPtr	att_depth;
	xmlAttrPtr	att_parent;

	att_name	 = xmlNewProp(cur, "id", name);
	att_depth        = xmlNewProp(cur, "depth", depth);
        att_parent   	 = xmlNewProp(cur, "parent", parent);

}

int createBurket(xmlNodePtr parent, int category, char *name, char *p_name, char *depth)
{

	//Check Parameter
	

	//Node Create
	xmlNodePtr node;

	if(category == 0)
		node = xmlNewNode(NULL, "folder");
	else
		node = xmlNewNode(NULL, "file");
	
	//Append Node
	printf("Start Append \n");
	xmlAddChild(parent, node);
	printf("End Append \n");

	//Set Attribute
	printf("Set Attributes \n");
	setAttributes(node, name, p_name, depth);
	printf("End Set Attributes \n");
}


char* showChildNodes(xmlDocPtr doc, xmlNodePtr cur, char *buf)
{
	//Check Parameter 
	if(doc == NULL)
		printf("doc is Null \n");

	if(cur == NULL)
		printf("cur is Null \n");

        if(buf == NULL)
		printf("buf is Null \n");   

	printf("Parameter OK \n");

	//Show Child Nodes
	xmlNodePtr node;
	
	int  category = 0;
	char tempString[1024];

	for(node = cur->xmlChildrenNode; node; node = node->next)
	{
		category = det_nodeCategory(node);
		memset(tempString, 0x00, 1024);

		//printf("debug \n");

		if(category == 0)
		{
			getNodeInfo(node, tempString); 
		    	//sprintf(buf, "%s", tempString);	
			strcat(buf, tempString);
		}
		else if(category == 1)
		{
			getNodeInfo(node, tempString);
			strcat(buf, tempString);
                        //sprintf(buf, "%s", tempString);	
		}
		else
		{


		}
	}

	return buf;
}
