import getSystemInfoSync from './getSystemInfoSync.js'

export function os() {
	return getSystemInfoSync().platform;
};

export function sys() {
	return getSystemInfoSync();
}


