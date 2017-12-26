package org.ekstep.graph.cache.mgr.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ekstep.common.exception.ClientException;
import org.ekstep.common.logger.PlatformLogger;
import org.ekstep.graph.cache.exception.GraphCacheErrorCodes;
import org.ekstep.graph.cache.util.CacheKeyGenerator;
import org.ekstep.graph.cache.util.RedisKeysEnum;

/**
 * 
 * @author Mahesh Kumar Gangula
 *
 */

public class NodeCacheManager {

	private static Map<String, Object> definitionNodeCache = new HashMap<>();
	private static Map<String, Object> dataNodeCache = new HashMap<>();

	public static void saveDefinitionNode(String graphId, String objectType, Object node) {
		validateRequired(graphId, objectType, node, GraphCacheErrorCodes.ERR_CACHE_SAVE_DEF_NODE_ERROR.name());
		String key = CacheKeyGenerator.getKey(graphId, objectType, RedisKeysEnum.DEF_NODE.name());
		definitionNodeCache.put(key, node);
		PlatformLogger.log("Saved definition node into cache having objectType: " + objectType + " into graph: "+ graphId);
	}

	public static Object getDefinitionNode(String graphId, String objectType) {
		validateRequired(graphId, objectType, GraphCacheErrorCodes.ERR_CACHE_GET_DEF_NODE_ERROR.name());
		String key = CacheKeyGenerator.getKey(graphId, objectType, RedisKeysEnum.DEF_NODE.name());
		PlatformLogger.log("Fetching definition node from cache having objectType: " + objectType + " in graph: "+ graphId);
		return definitionNodeCache.get(key);
	}
	
	public static Object deleteDefinitionNode(String graphId, String objectType) {
		validateRequired(graphId, objectType, GraphCacheErrorCodes.ERR_CACHE_GET_DEF_NODE_ERROR.name());
		String key = CacheKeyGenerator.getKey(graphId, objectType, RedisKeysEnum.DEF_NODE.name());
		PlatformLogger.log("Deleting definition node from cache having objectType: " + objectType + " in graph: "+ graphId);
		return definitionNodeCache.remove(key);
	}

	public static void saveDataNode(String graphId, String id, Object node) {
		validateRequired(graphId, id, node, GraphCacheErrorCodes.ERR_CACHE_SAVE_DEF_NODE_ERROR.name());
		String key = CacheKeyGenerator.getKey(graphId, id, RedisKeysEnum.DATA_NODE.name());
		dataNodeCache.put(key, node);
		PlatformLogger.log("Saved data node into cache having identifier: " + id + " into graph: "+ graphId);
	}

	public static Object getDataNode(String graphId, String id) {
		validateRequired(graphId, id, GraphCacheErrorCodes.ERR_CACHE_SAVE_DEF_NODE_ERROR.name());
		String key = CacheKeyGenerator.getKey(graphId, id, RedisKeysEnum.DATA_NODE.name());
		PlatformLogger.log("Fetching data node from cache having identifier: " + id + " in graph: "+ graphId);
		return dataNodeCache.get(key);
	}
	
	public static Object deleteDataNode(String graphId, String id) {
		validateRequired(graphId, id, GraphCacheErrorCodes.ERR_CACHE_SAVE_DEF_NODE_ERROR.name());
		String key = CacheKeyGenerator.getKey(graphId, id, RedisKeysEnum.DATA_NODE.name());
		PlatformLogger.log("Deleting data node from cache having identifier: " + id + " in graph: "+ graphId);
		return dataNodeCache.remove(key);
	}

	private static void validateRequired(String graphId, String id, Object members, String errCode) {
		validateRequired(graphId, id, errCode);
		if (null == members)
			throw new ClientException(errCode, "node is null.");
	}

	private static void validateRequired(String graphId, String id, String errCode) {
		if (StringUtils.isBlank(graphId))
			throw new ClientException(errCode, "graphId is missing");
		if (StringUtils.isBlank(id))
			throw new ClientException(errCode, "id/objectType is missing");
	}
}