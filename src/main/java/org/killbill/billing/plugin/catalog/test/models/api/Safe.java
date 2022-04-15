package org.killbill.billing.plugin.catalog.test.models.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Objects;
import java.util.Iterator;

import org.killbill.billing.catalog.api.CatalogEntity;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.StaticCatalog;

class Safe {
    private Safe() { }
    private static void append(StringBuilder sb, String value){
        if(value == null ){
            sb.append(value);
        } else {
            sb.append("'").append(value).append("'");
        }
    }
    private static List<CatalogEntity> list(Collection<? extends CatalogEntity> value) {
        ArrayList<CatalogEntity>  output = null;
        if(value != null) {
            output = new ArrayList<CatalogEntity>(value);
        }
        return output;
    }
    private static List<CatalogEntity> list(Iterator<? extends CatalogEntity> value) {
        ArrayList<CatalogEntity>  output = null;
        if(value != null) {
            output = new ArrayList<CatalogEntity>();
            while(value.hasNext()){
                output.add(value.next());
            }
        }
        return output;
    }
    //Iterator<? extends CatalogEntity>
    public static boolean equals(final Iterator<? extends CatalogEntity> lhs, final Iterator<? extends CatalogEntity> rhs) {
        return equals(list(lhs), list(rhs));
    }
    public static int hashCode(final Iterator<? extends CatalogEntity> value) {
        return hashCode(list(value));
    }
    public static String toString(final Iterator<? extends CatalogEntity> value) {
        return toString(list(value));
    }
    // Collection<? extends CatalogEntity> 
    public static boolean equals(final Collection<? extends CatalogEntity> lhs, final Collection<? extends CatalogEntity> rhs) {
        if(lhs == rhs ) return true;
        if(lhs == null || rhs == null) return false;

        List<? extends CatalogEntity> left = list(lhs);
        List<? extends CatalogEntity> right = list(rhs);

        if(left.size() != right.size()) {
            return false;
        }
        for(int i = 0; i < left.size() ; i++) {
            if(!equals(left.get(i), right.get(i))) {
                return false;
            }
        }
        return true;
    }
    public static int hashCode(final Collection<? extends CatalogEntity> value) {
        int result = (value == null) ? Objects.hashCode(value) : 1;
        if(value != null) {
            for(CatalogEntity element : value) {
                result = ( 31 * result ) + hashCode(element);
            }
        }
        return result;
    }
    public static String toString(final Collection<? extends CatalogEntity> value) {
        final StringBuilder sb = new StringBuilder();
        if(value == null) {
            sb.append(value);
        }else{
            sb.append("[");
            for(CatalogEntity element : value) {
                sb.append(toString(element));
            }
            sb.append("]");
        }
        return sb.toString();
    }
    // CatalogEntity
    public static boolean equals(final CatalogEntity lhs, final CatalogEntity rhs) {
        if(lhs == rhs) {
            return true;
        }
        if((lhs == null) || (rhs == null)) {
            return false;
        }
        if(lhs.getClass() != rhs.getClass() ) { 
            return false;
        }
        if(!Objects.equals(lhs.getName(), rhs.getName())) {
            return false;
        }
        if(!Objects.equals(lhs.getPrettyName(), rhs.getPrettyName())) {
            return false;
        }
        return true;
    }
    public static int hashCode(final CatalogEntity value) {
        int result = (value == null) ? Objects.hashCode(value) : 1;
        if(value != null) {
            result = ( 31 * result ) + Objects.hashCode(value.getName());
            result = ( 31 * result ) + Objects.hashCode(value.getPrettyName());
        }
        return result;
    }
    public static String toString(final CatalogEntity value) {
        final StringBuilder sb = new StringBuilder();
        if(value == null) {
            sb.append(value);
        }else{
            append(sb, value.getName());
        }
        return sb.toString();
    }
    // StaticCatalog
    public static boolean equals(final StaticCatalog lhs, final StaticCatalog rhs) {
        if(lhs == rhs) {
            return true;
        }
        if((lhs == null) || (rhs == null)) {
            return false;
        }
        if(lhs.getClass() != rhs.getClass() ) { 
            return false;
        }
        return Objects.equals(lhs.getCatalogName(), rhs.getCatalogName()); 
    }
    public static int hashCode(final StaticCatalog value) {
        return (value == null) ? Objects.hashCode(value) : Objects.hashCode(value.getCatalogName());
    }
    public static String toString(final StaticCatalog value) {
        final StringBuilder sb = new StringBuilder();
        if(value == null) {
            sb.append(value);
        }else{
            append(sb, value.getCatalogName());
        }
        return sb.toString();
    }
}

