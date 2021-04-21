package com.root.onvif.mbg.model;

import java.util.ArrayList;
import java.util.List;

public class TbDevExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbDevExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andPortIsNull() {
            addCriterion("port is null");
            return (Criteria) this;
        }

        public Criteria andPortIsNotNull() {
            addCriterion("port is not null");
            return (Criteria) this;
        }

        public Criteria andPortEqualTo(Integer value) {
            addCriterion("port =", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotEqualTo(Integer value) {
            addCriterion("port <>", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortGreaterThan(Integer value) {
            addCriterion("port >", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("port >=", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLessThan(Integer value) {
            addCriterion("port <", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLessThanOrEqualTo(Integer value) {
            addCriterion("port <=", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortIn(List<Integer> values) {
            addCriterion("port in", values, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotIn(List<Integer> values) {
            addCriterion("port not in", values, "port");
            return (Criteria) this;
        }

        public Criteria andPortBetween(Integer value1, Integer value2) {
            addCriterion("port between", value1, value2, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotBetween(Integer value1, Integer value2) {
            addCriterion("port not between", value1, value2, "port");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressIsNull() {
            addCriterion("onvif_address is null");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressIsNotNull() {
            addCriterion("onvif_address is not null");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressEqualTo(String value) {
            addCriterion("onvif_address =", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressNotEqualTo(String value) {
            addCriterion("onvif_address <>", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressGreaterThan(String value) {
            addCriterion("onvif_address >", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressGreaterThanOrEqualTo(String value) {
            addCriterion("onvif_address >=", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressLessThan(String value) {
            addCriterion("onvif_address <", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressLessThanOrEqualTo(String value) {
            addCriterion("onvif_address <=", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressLike(String value) {
            addCriterion("onvif_address like", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressNotLike(String value) {
            addCriterion("onvif_address not like", value, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressIn(List<String> values) {
            addCriterion("onvif_address in", values, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressNotIn(List<String> values) {
            addCriterion("onvif_address not in", values, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressBetween(String value1, String value2) {
            addCriterion("onvif_address between", value1, value2, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andOnvifAddressNotBetween(String value1, String value2) {
            addCriterion("onvif_address not between", value1, value2, "onvifAddress");
            return (Criteria) this;
        }

        public Criteria andMediaUrlIsNull() {
            addCriterion("media_url is null");
            return (Criteria) this;
        }

        public Criteria andMediaUrlIsNotNull() {
            addCriterion("media_url is not null");
            return (Criteria) this;
        }

        public Criteria andMediaUrlEqualTo(String value) {
            addCriterion("media_url =", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlNotEqualTo(String value) {
            addCriterion("media_url <>", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlGreaterThan(String value) {
            addCriterion("media_url >", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlGreaterThanOrEqualTo(String value) {
            addCriterion("media_url >=", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlLessThan(String value) {
            addCriterion("media_url <", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlLessThanOrEqualTo(String value) {
            addCriterion("media_url <=", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlLike(String value) {
            addCriterion("media_url like", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlNotLike(String value) {
            addCriterion("media_url not like", value, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlIn(List<String> values) {
            addCriterion("media_url in", values, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlNotIn(List<String> values) {
            addCriterion("media_url not in", values, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlBetween(String value1, String value2) {
            addCriterion("media_url between", value1, value2, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andMediaUrlNotBetween(String value1, String value2) {
            addCriterion("media_url not between", value1, value2, "mediaUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlIsNull() {
            addCriterion("imaging_url is null");
            return (Criteria) this;
        }

        public Criteria andImagingUrlIsNotNull() {
            addCriterion("imaging_url is not null");
            return (Criteria) this;
        }

        public Criteria andImagingUrlEqualTo(String value) {
            addCriterion("imaging_url =", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlNotEqualTo(String value) {
            addCriterion("imaging_url <>", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlGreaterThan(String value) {
            addCriterion("imaging_url >", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlGreaterThanOrEqualTo(String value) {
            addCriterion("imaging_url >=", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlLessThan(String value) {
            addCriterion("imaging_url <", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlLessThanOrEqualTo(String value) {
            addCriterion("imaging_url <=", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlLike(String value) {
            addCriterion("imaging_url like", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlNotLike(String value) {
            addCriterion("imaging_url not like", value, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlIn(List<String> values) {
            addCriterion("imaging_url in", values, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlNotIn(List<String> values) {
            addCriterion("imaging_url not in", values, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlBetween(String value1, String value2) {
            addCriterion("imaging_url between", value1, value2, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andImagingUrlNotBetween(String value1, String value2) {
            addCriterion("imaging_url not between", value1, value2, "imagingUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlIsNull() {
            addCriterion("events_url is null");
            return (Criteria) this;
        }

        public Criteria andEventsUrlIsNotNull() {
            addCriterion("events_url is not null");
            return (Criteria) this;
        }

        public Criteria andEventsUrlEqualTo(String value) {
            addCriterion("events_url =", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlNotEqualTo(String value) {
            addCriterion("events_url <>", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlGreaterThan(String value) {
            addCriterion("events_url >", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlGreaterThanOrEqualTo(String value) {
            addCriterion("events_url >=", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlLessThan(String value) {
            addCriterion("events_url <", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlLessThanOrEqualTo(String value) {
            addCriterion("events_url <=", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlLike(String value) {
            addCriterion("events_url like", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlNotLike(String value) {
            addCriterion("events_url not like", value, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlIn(List<String> values) {
            addCriterion("events_url in", values, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlNotIn(List<String> values) {
            addCriterion("events_url not in", values, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlBetween(String value1, String value2) {
            addCriterion("events_url between", value1, value2, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andEventsUrlNotBetween(String value1, String value2) {
            addCriterion("events_url not between", value1, value2, "eventsUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlIsNull() {
            addCriterion("device_url is null");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlIsNotNull() {
            addCriterion("device_url is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlEqualTo(String value) {
            addCriterion("device_url =", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlNotEqualTo(String value) {
            addCriterion("device_url <>", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlGreaterThan(String value) {
            addCriterion("device_url >", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlGreaterThanOrEqualTo(String value) {
            addCriterion("device_url >=", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlLessThan(String value) {
            addCriterion("device_url <", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlLessThanOrEqualTo(String value) {
            addCriterion("device_url <=", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlLike(String value) {
            addCriterion("device_url like", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlNotLike(String value) {
            addCriterion("device_url not like", value, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlIn(List<String> values) {
            addCriterion("device_url in", values, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlNotIn(List<String> values) {
            addCriterion("device_url not in", values, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlBetween(String value1, String value2) {
            addCriterion("device_url between", value1, value2, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andDeviceUrlNotBetween(String value1, String value2) {
            addCriterion("device_url not between", value1, value2, "deviceUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlIsNull() {
            addCriterion("ptz_url is null");
            return (Criteria) this;
        }

        public Criteria andPtzUrlIsNotNull() {
            addCriterion("ptz_url is not null");
            return (Criteria) this;
        }

        public Criteria andPtzUrlEqualTo(String value) {
            addCriterion("ptz_url =", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlNotEqualTo(String value) {
            addCriterion("ptz_url <>", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlGreaterThan(String value) {
            addCriterion("ptz_url >", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlGreaterThanOrEqualTo(String value) {
            addCriterion("ptz_url >=", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlLessThan(String value) {
            addCriterion("ptz_url <", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlLessThanOrEqualTo(String value) {
            addCriterion("ptz_url <=", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlLike(String value) {
            addCriterion("ptz_url like", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlNotLike(String value) {
            addCriterion("ptz_url not like", value, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlIn(List<String> values) {
            addCriterion("ptz_url in", values, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlNotIn(List<String> values) {
            addCriterion("ptz_url not in", values, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlBetween(String value1, String value2) {
            addCriterion("ptz_url between", value1, value2, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andPtzUrlNotBetween(String value1, String value2) {
            addCriterion("ptz_url not between", value1, value2, "ptzUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlIsNull() {
            addCriterion("analytics_url is null");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlIsNotNull() {
            addCriterion("analytics_url is not null");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlEqualTo(String value) {
            addCriterion("analytics_url =", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlNotEqualTo(String value) {
            addCriterion("analytics_url <>", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlGreaterThan(String value) {
            addCriterion("analytics_url >", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlGreaterThanOrEqualTo(String value) {
            addCriterion("analytics_url >=", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlLessThan(String value) {
            addCriterion("analytics_url <", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlLessThanOrEqualTo(String value) {
            addCriterion("analytics_url <=", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlLike(String value) {
            addCriterion("analytics_url like", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlNotLike(String value) {
            addCriterion("analytics_url not like", value, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlIn(List<String> values) {
            addCriterion("analytics_url in", values, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlNotIn(List<String> values) {
            addCriterion("analytics_url not in", values, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlBetween(String value1, String value2) {
            addCriterion("analytics_url between", value1, value2, "analyticsUrl");
            return (Criteria) this;
        }

        public Criteria andAnalyticsUrlNotBetween(String value1, String value2) {
            addCriterion("analytics_url not between", value1, value2, "analyticsUrl");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}