const API_URLS = {
  uat: "http://uat.tiansiwork.live",
  ga: "http://ga.tiansiwork.live",
};

// Use a runtime hint from the browser (hostname, subdomain, etc.)
const getCurrentEnv = () => {
  const host = window.location.hostname;
  console.log("getCurrentEnv is called");
  console.log("Host is:", host);
  if (host.startsWith("uat.")) return "uat";
  if (host.startsWith("ga.")) return "ga";
  if (host.startsWith("green.")) return "green";

  // Fallback
  return "ga";
};

const API_URL = API_URLS[getCurrentEnv()];

console.log("config.js is loaded");

export default API_URL;
