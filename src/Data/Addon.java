/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author BJ
 */
public class Addon {
    private String description;
    private String version;
    private String slug;
    private String name;
    private String installed="-";
    private java.util.ArrayList tags=new java.util.ArrayList();
    public Addon(javax.json.JsonObject addon) {
        description=getStringFromObject("description",addon);
        version=getStringFromObject("version",addon);
        slug=getStringFromObject("slug",addon);
        name=getStringFromObject("name",addon);
        javax.json.JsonArray tagList=addon.getJsonArray("tags");
        int counter=0;
        while(tagList.size()>counter) {
            tags.add(tagList.getString(counter));
            counter++;
        }
        findInstalled();
    }
    protected java.io.File findMatch(java.io.File folder,String search) {
            if(!folder.exists()) {
                return null;
            }
            for(java.io.File innerFolder : folder.listFiles()) {
                if(innerFolder.getName().equalsIgnoreCase(search)) {
                    return innerFolder;
                }
            }
            return null;
    }
    protected java.io.File getAddonFolder() {
        String[] path= new String[3];
        path[0]="interface";
        path[1]="addons";
        path[2]=name;
        java.io.File folder = new java.io.File("./");
        for(int counter=0;counter<3;counter++) {
            folder=findMatch(folder,path[counter]);
            if(folder==null||!folder.exists()) {
                return null;
            }
        }
        return folder;
    }
    public final void findInstalled() {
        java.io.File folder=getAddonFolder();
        if(folder==null||!folder.exists()) {
            return;
        }
        for (java.io.File fileEntry : folder.listFiles()) {
            if (
                !fileEntry.isDirectory() &&
                org.apache.commons.io.FilenameUtils.getExtension(fileEntry.getName()).equalsIgnoreCase("mod")
            ){
                try{
                    org.w3c.dom.NodeList list=javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileEntry).getElementsByTagName("UiMod");
                    for(int counter=0;counter<list.getLength();counter++) {
                        this.installed=list.item(counter).getAttributes().getNamedItem("version").getTextContent();
                    }
                }
                catch(javax.xml.parsers.ParserConfigurationException | javax.xml.parsers.FactoryConfigurationError | org.xml.sax.SAXException | java.io.IOException exception){
                    System.out.println(exception.getMessage());
                }
            }
        }
    }
    protected final String getStringFromObject(String key,javax.json.JsonObject data) {
        String value="";
        if(!data.isNull(key)) {
            value=data.getString(key);
        }
        return value;
    }
    public String[] getTableRow() {
        String[] row=new String[3];
        row[0]=this.name;
        row[1]=this.version;
        row[2]=this.installed;
        return row;
    }
    public String getDescription() {
        return "<html>"+description;
    }
    public String getName() {
        return name;
    }
    public String getDownloadLink() {
        return "http://tools.idrinth.de/addons/"+slug+"/download/"+version.replace(".", "-")+"/";
    }
}
