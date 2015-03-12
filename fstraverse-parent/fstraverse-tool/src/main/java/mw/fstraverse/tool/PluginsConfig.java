package mw.fstraverse.tool;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="plugin" maxOccurs="25" minOccurs="2">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="jarURI" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="pluginClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "plugin"
})
@XmlRootElement(name = "plugins")
public class PluginsConfig {

    @XmlElement(required = true)
    protected List<PluginsConfig.Plugin> plugin;

    /**
     * Gets the value of the plugin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the plugin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlugin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PluginsConfig.Plugin }
     * 
     * 
     */
    public List<PluginsConfig.Plugin> getPlugin() {
        if (plugin == null) {
            plugin = new ArrayList<PluginsConfig.Plugin>();
        }
        return this.plugin;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="jarURI" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="pluginClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "id",
        "jarURI",
        "pluginClass"
    })
    public static class Plugin {

        public Plugin() {
        }

        @Override
        public String toString() {
            return "Plugin [id=" + id + ", jarURI=" + jarURI + ", pluginClass="
                    + pluginClass + "]";
        }

        public Plugin(String id, String jarURI, String pluginClass) {
            super();
            this.id = id;
            this.jarURI = jarURI;
            this.pluginClass = pluginClass;
        }

        @XmlElement(required = true)
        protected String id;
        @XmlElement(required = true)
        protected String jarURI;
        @XmlElement(required = true)
        protected String pluginClass;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the jarURI property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getJarURI() {
            return jarURI;
        }

        /**
         * Sets the value of the jarURI property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setJarURI(String value) {
            this.jarURI = value;
        }

        /**
         * Gets the value of the pluginClass property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPluginClass() {
            return pluginClass;
        }

        /**
         * Sets the value of the pluginClass property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPluginClass(String value) {
            this.pluginClass = value;
        }

    }

}
